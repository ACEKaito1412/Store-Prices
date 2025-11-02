import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

class EmailSender:
    def __init__(self, smtp_server, smtp_port, email, password):
        self.smtp_server = smtp_server
        self.smtp_port = smtp_port
        self.email = email
        self.password = password

    def send_email(self, recipient, subject, body):
        # Create the email message
        msg = MIMEMultipart()
        msg['From'] = self.email
        msg['To'] = recipient
        msg['Subject'] = subject

        # Attach the email body
        msg.attach(MIMEText(body, 'plain'))

        try:
            # Connect to SMTP server
            with smtplib.SMTP(self.smtp_server, self.smtp_port) as server:
                server.starttls()  # Secure connection
                server.login(self.email, self.password)
                server.send_message(msg)
                print(f"✅ Email sent successfully to {recipient}")
        except Exception as e:
            print(f"❌ Failed to send email: {e}")
