from functools import wraps
from flask import redirect, url_for
from flask_login import current_user
from random import randint

def activation_required(f):
    @wraps(f)
    def decorated_function(*args, **kwargs):
        if not current_user.activated:
            return redirect(url_for('index.activate'))
        return f(*args, **kwargs)
    return decorated_function


def generate_otp():
    key = ""
    for i in range(4):
        key += str(randint(1,9))
    return key