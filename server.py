from flask import Flask, request, jsonify
import uuid

app = Flask(__name__)

#   SERVER UNDE SE DUC REQ. HTTP
messages = [
    # {'id': '1000',
    #  'senderId': 'DOCTOR-ID',
    #  'receiverId': 'PATIENT-ID',
    #  'message': 'Nu uitati sa va luati tratamentul la ora 18!',
    #  'timeSent': '2025-05-24'},
    #
    # {'id': '1001',
    #  'senderId': 'PATIENT-ID',
    #  'receiverId': 'DOCTOR-ID',
    #  'message': 'Multumesc ca mi-ati amintit!',
    #  'timeSent': '2025-05-24'},
    #
    # {'id': '1002',
    #  'senderId': 'PATIENT-ID',
    #  'receiverId': 'DOCTOR-ID',
    #  'message': 'O zi buna!',
    #  'timeSent': '2025-05-24'},
    #
    # {'id': '1003',
    #  'senderId': 'DOCTOR-ID',
    #  'receiverId': 'PATIENT-ID',
    #  'message': 'Nu aveti pentru ce! O zi buna de asemenea!',
    #  'timeSent': '2025-05-24'}
]


@app.route('/api/messages', methods = ['POST'])
def send_message():
    data = request.json
    required_fields = ['id', 'senderId', 'receiverId', 'message', 'timeSent']
    if not all(field in data for field in required_fields):
        return jsonify({"error": "Missing fields"}), 400

    print(data)

    messages.append(data)
    return jsonify({"response": "Message saved successfully"}), 200


@app.route('/api/messages', methods=['GET'])
def get_messages():
    p1Id = request.args.get('p1Id')
    p2Id = request.args.get('p2Id')

    if not p1Id or not p2Id:
        return jsonify({"error": "Missing p1Id or p2Id"}), 400

    filtered = [
        msg for msg in messages
        if (msg["senderId"] == p1Id and msg["receiverId"] == p2Id) or
           (msg["senderId"] == p2Id and msg["receiverId"] == p1Id)
    ]

    return jsonify({"data": filtered}), 200



if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8080)