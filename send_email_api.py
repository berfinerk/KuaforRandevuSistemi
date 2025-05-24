from flask import Flask, request, jsonify
import smtplib
from email.mime.text import MIMEText
import traceback

app = Flask(__name__)

@app.route('/send_reset_email', methods=['POST'])
def send_reset_email():
    data = request.json
    to_email = data.get('email')
    code = data.get('code')

    gmail_user = 'bitirmeodev04@gmail.com'  # Kendi gmail adresin
    gmail_password = 'rxyrocptsbmoqnfv'  # Oluşturduğun uygulama şifresi

    msg = MIMEText(f"Şifre sıfırlama kodunuz: {code}")
    msg['Subject'] = 'Şifre Sıfırlama'
    msg['From'] = gmail_user
    msg['To'] = to_email

    try:
        server = smtplib.SMTP_SSL('smtp.gmail.com', 465)
        server.login(gmail_user, gmail_password)
        server.sendmail(gmail_user, [to_email], msg.as_string())
        server.quit()
        return jsonify({'success': True}), 200
    except Exception as e:
        print("Exception occurred:", e)
        traceback.print_exc()
        return jsonify({'success': False, 'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000) 