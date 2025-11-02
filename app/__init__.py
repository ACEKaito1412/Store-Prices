from flask import Flask, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from flask_login import LoginManager, login_manager
from app.service.smtp import EmailSender
from dotenv import load_dotenv
import os



load_dotenv()

db = SQLAlchemy()
login_manager = LoginManager()

@login_manager.user_loader
def load_user(user_id):
    from app.model.Models import User
    return db.get_or_404(User, user_id)

def create_app():
    app = Flask(__name__)

    app.config.from_object('app.configs.Config')

    sender = EmailSender(
        smtp_server="smtp.gmail.com",
        smtp_port=587,
        email= os.getenv("SMTP_MAIL"),
        password=os.getenv("SMTP_PASS")
    )
    
    login_manager.init_app(app)
    login_manager.login_view = 'index.login'

    db.init_app(app)

    from app.routes.index import index_bp, init_index
    from app.routes.item import item_bp
    from app.routes.reciepts import reciept_bp

    init_index(sender)

    app.register_blueprint(index_bp)
    app.register_blueprint(item_bp, url_prefix="/item")
    app.register_blueprint(reciept_bp, url_prefix="/reciept")

    return app