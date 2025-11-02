import os
from dotenv import load_dotenv

load_dotenv()

class Config:
    SECRET_KEY = "secreto-keto"
    SQLALCHEMY_DATABASE_URI = "sqlite:///store_prices.db"
    SQLALCHEMY_TRACK_MODIFICATIONS = True
    DEBUG = True