from flask import Blueprint, render_template, request, redirect, flash, url_for
from flask_login import login_user, login_required, current_user, logout_user
from werkzeug.security import generate_password_hash, check_password_hash
from app.model.Models import User, Item, Receipt, ReceiptTypeEnum
from sqlalchemy import and_, desc
from app import db
from app.utils import activation_required, generate_otp
from app.service.smtp import EmailSender
from datetime import datetime, timedelta
import json

_mailSender = None

index_bp = Blueprint('index', __name__)

def init_index(MailSender:EmailSender):
    global _mailSender
    _mailSender = MailSender

@index_bp.route("/")
@login_required
@activation_required
def home():
    items = Item.query.filter_by(user_id=current_user.id).order_by(Item.name.asc()).all()

    dept = Receipt.query.filter(and_(
        Receipt.user_id == current_user.id,
        Receipt.type == ReceiptTypeEnum.UNPAID
    )).order_by(Receipt.name.asc()).all()


    return render_template('index.html', data=items, dept_data = dept)


@index_bp.route("/login", methods = ["GET", "POST"])
def login():
    if request.method == 'POST':
        email = request.form.get('email')
        password = request.form.get('password')
        try:
            res = db.session.execute(db.select(User).where(User.email == email)).scalar()
            # print(res.email)
            if check_password_hash(res.password, password):
                login_user(res)
                return redirect('/')
            else:
                flash("Wrong Password")
                return redirect('/login')
            
        except AttributeError:
            flash("No user found")
            return redirect('/login')
    return render_template('login.html')


@index_bp.route("/register", methods = ['GET', 'POST'])
def signup():
    if request.method == "POST":
        email = request.form.get("email")
        otp = generate_otp()
        new_user = User(
            email = email,
            password = generate_password_hash(request.form.get("password"), method="pbkdf2", salt_length=8),
            otp = otp
        )

        try:
            db.session.add(new_user)
            db.session.commit()
        except Exception as e:
            flash(f"Error occured while creating a user {e}")

            return redirect(url_for('index.login'))

        _mailSender.send_email(
            recipient=email,
            subject="Activation key for Shop Prices",
            body=f"Hi, this would be your activation key for Shop Prices.<br><h1>{otp}</h1><br>Thank you."
        )


        login_user(new_user)
        return redirect('/')
    
    return render_template('signup.html')


@index_bp.route("/activate", methods =["GET", "POST"])
def activate():

    if request.method == "POST":
        code = request.form.get("code")

        user = User.query.filter_by(id=current_user.id).first()
        # print(f"{code} {current_user.otp} {user.activated}")
        
        if current_user.otp == code:
            user.activated = True
            user.otp = ""

            db.session.commit()
            login_user(user)

            return redirect(url_for('index.home'))
        else:
            flash("Wrong code")
    
    return render_template('activation.html')


@index_bp.route('/receipt', methods=['POST','GET'])
@login_required
@activation_required
def receipt():
    
    startDate = datetime.now().date()
    endDate = startDate + timedelta(days=1)
    data = []
    form_data = {}

    if request.method == "POST":
        form_data = request.form.to_dict()

        startDate = datetime.strptime(form_data['startDate'], "%Y-%m-%d")
        endDate = datetime.strptime(form_data['endDate'], "%Y-%m-%d") + timedelta(days=1)


        s_type = ReceiptTypeEnum(form_data['type'])

        res = Receipt.query.filter(
        and_(
            Receipt.user_id == current_user.id,
            Receipt.type == ReceiptTypeEnum(form_data['type']),
            Receipt.updated_date >= startDate,
            Receipt.updated_date < endDate
        )).order_by(desc(Receipt.updated_date)).all()

        print(f"{startDate} : {endDate} : {s_type} : {current_user.id}")
    else:

        res = Receipt.query.filter(
        and_(
            Receipt.user_id == current_user.id,
            Receipt.updated_date >= f"{startDate}",
            Receipt.updated_date < f"{endDate}",
        )).order_by(desc(Receipt.updated_date)).all()


    for receipt in res:
            receipt_dict = {
                "id": receipt.id,
                "name": receipt.name,
                "items": json.loads(receipt.items),  # parse JSON here
                "total": receipt.total,
                "change": receipt.change,
                "cash" : receipt.cash,
                "updated_date" : receipt.updated_date.date()
            }
            data.append(receipt_dict)

    overallTotal = {
        'overallTotal' : sum(item['total'] for item in data),
        'overallChanges' : sum(item['change'] for item in data),
        'overallCash' : sum(item['cash'] for item in data)
        }

    return render_template('receipt.html', data=data, overallTotal = overallTotal, form_data = form_data)

@index_bp.route('/logout')
def logout():
    logout_user()
    return redirect(url_for('index.login'))

