from flask import Blueprint, request, redirect, url_for, jsonify, render_template
from flask_login import current_user
from app.model.Models import Receipt, ReceiptTypeEnum
from app import db
import json

reciept_bp = Blueprint('reciept', __name__)


@reciept_bp.route('/add/', methods=['POST'])
def add():
    data = request.get_json()

    new_dept = Receipt(
        user_id = current_user.id,
        type = ReceiptTypeEnum.UNPAID,
        name = data.get('name'),
        items = data.get('items'),
        total = data.get('total')
    )

    db.session.add(new_dept)
    db.session.commit()

    return jsonify({'status' : 'success', 'message' : "JSON saved"})



@reciept_bp.route('/pay/<string:type>', methods=['POST'])
def pay(type):
    data = request.get_json()

    if type == 'dept':
        reciept_id = data.get('id')
        reciept = Receipt.query.get(reciept_id)

        if reciept:
            reciept.type = ReceiptTypeEnum.PAID
            reciept.name = data.get('name')
            reciept.items = data.get('items')
            reciept.cash = data.get('cash')
            reciept.total = data.get('total')
            reciept.change = data.get('change')

            db.session.commit()
            return jsonify({'status' : 'success', 'message' : 'JSON recieved'})
        else:

            return jsonify({'status' : 'failed', 'message' : 'No item found'})
        
    else:
        s_type = data.get("selected")
        typeEnum = ReceiptTypeEnum.PAID


        if s_type == "cashin":
            typeEnum = ReceiptTypeEnum.CASHIN
        elif s_type == "cashout":
            typeEnum = ReceiptTypeEnum.CASHOUT

        new_reciept = Receipt(
            user_id = current_user.id,
            type = typeEnum,
            items = data.get('items'),
            name = data.get('name'),
            cash = data.get('cash'),
            total = data.get('total'),
            change = data.get('change'),
        )

        db.session.add(new_reciept)

    db.session.commit()

    return jsonify({'status' : 'success', 'message' : "JSON saved"})


@reciept_bp.route('/update/', methods=["POST"])
def update():
    data = request.get_json()

    try:
        reciept_id = data.get('id')
        reciept = Receipt.query.get(reciept_id)
        if reciept:
            reciept.type = ReceiptTypeEnum.UNPAID
            reciept.name = data.get('name')
            reciept.items = data.get('items')
            reciept.cash = data.get('cash')
            reciept.total = data.get('total')
            reciept.change = data.get('change')

            db.session.commit()
            return jsonify({'status' : 'success', 'message' : 'JSON recieved'})

        else:

            return jsonify({'status' : 'failed', 'message' : 'No item found'})

    except Exception as e:
        return jsonify({'status' : 'failed', 'message' : e})


@reciept_bp.route('/delete/<int:id>', methods=['GET'])
def delete(id):

    try:
        reciept = Receipt.query.get(id)

        if reciept:
            db.session.delete(reciept)
            db.session.commit()
            return jsonify({'status' : 'success', 'message' : 'JSON recieved'})

        else:
            return jsonify({'status' : 'failed', 'message' : 'No reciept found'})

    except Exception as e:
        return jsonify({'status' : 'failed', 'message' : str(e)})
    

@reciept_bp.route('get/<string:id>')
def get(id):

    res = Receipt.query.filter_by(id=id).first()

    data = {
        'id' : res.id,
        'name' : res.name,
        'items' : json.loads(res.items)
    }

    return render_template('_reciept_items.html', data_reciept = data)