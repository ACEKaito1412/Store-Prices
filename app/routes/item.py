from flask import Blueprint, redirect, url_for, request, jsonify, render_template
from sqlalchemy import and_
from flask_login import current_user
from app.model.Models import Item
from app import db

item_bp = Blueprint('item', __name__)


@item_bp.route('/add', methods=["POST"])
def add():
    data = request.get_json()

    try:
        new_item = Item(
            user_id = current_user.id,
            name = data.get('name'),
            price = float(data.get('price'))
        )

        db.session.add(new_item)
        db.session.commit()
        return jsonify({'status' : 'success', 'message' : 'JSON recieved'})
    except Exception as e:
        return jsonify({'status' : 'failed', 'message' : e})

@item_bp.route('/update', methods=["POST"])
def update():
    data = request.get_json()

    print(data)
    try:
        item_id = int(data.get('id'))
        item = Item.query.get(item_id)

        if item:
            item.name = data.get('name')
            item.price = float(data.get('price'))

            db.session.commit()
            return jsonify({'status' : 'success', 'message' : 'JSON recieved'})

        else:

            return jsonify({'status' : 'failed', 'message' : 'No item found'})

    except Exception as e:
        return jsonify({'status' : 'failed', 'message' : e})


@item_bp.route('/delete/<int:id>', methods=['GET'])
def delete(id):

    try:
        item = Item.query.get(id)

        if item:
            db.session.delete(item)
            db.session.commit()
            return jsonify({'status' : 'success', 'message' : 'JSON recieved'})

        else:
            return jsonify({'status' : 'failed', 'message' : 'No item found'})

    except Exception as e:
        return jsonify({'status' : 'failed', 'message' : str(e)})
    
@item_bp.route("/search", methods=["GET"])
def search():
    q = request.args.get("item", "").lower()

    if q == "" or q == " ":
        res = Item.query.filter_by(user_id = current_user.id).all()
    else:   
        res = Item.query.filter(
            and_(Item.user_id == current_user.id, 
                 Item.name.ilike(f"%{q}%"))
            ).all()
        
    return render_template("_items_list.html", data = res)