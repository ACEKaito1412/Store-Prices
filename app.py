from app import create_app, db  
from app.utils import generate_otp

app = create_app()

if '__main__' == __name__:
    with app.app_context():
        db.create_all()

    app.run(debug=True, host="0.0.0.0", port=5002)