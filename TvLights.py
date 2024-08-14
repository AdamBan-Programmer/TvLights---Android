import json
import socket
import time
import board
import neopixel

def getSocket():
    server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    server.bind((server_ip, port))
    server.listen(0)
    return server

def handleClient(client_socket):
    try:
        while True:
            request = client_socket.recv(999999)
            if not request:
                break
            request_str = request.decode("utf-8")
            data = json.loads(request_str)
            if data != "close":
                hex_color = data.replace('#','')
                NeoPixel.fill(int(hex_color,16))
                NeoPixel.show()
            else:
                break
    except Exception as e:
        print(e)

server_ip = "xxx"   #example: 192.168.0.22
port = xxx  #example: 56333
GPIO = 21
strip_length = xxx  #example: 145
server = getSocket()
NeoPixel = neopixel.NeoPixel(board.D21,strip_length,brightness=0.5, auto_write=False, pixel_order=neopixel.GRB)

while True:
    client_socket, client_address = server.accept()
    handleClient(client_socket)
