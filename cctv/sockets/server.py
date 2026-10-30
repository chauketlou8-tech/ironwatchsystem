from .camera.camera_capture import capture_frame
from .camera.camera_manager import init_camera

class Server:
    def __init__(self):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind(('localhost', 4001))

        self.server.listen()
        print("waiting for connection...")

        conn, addr = self.server.accept()
        print("connected from", addr)

        self.camera = init_camera()

        while True:
            data = conn.recv(1024)
            data = data.decode()

            match data:
                case 'start':
                    ret, frame = capture_frame(camera)
                    conn.send(frame.encode())

                case 'stop':
                    pass