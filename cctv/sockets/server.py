from recording.video_recorder import VideoRecorder
from streaming.video_streamer import Streamer
from camera.camera_capture import capture_frame
from camera.camera_capture import capture_image as capture
from camera.camera_manager import init_camera
from datetime import datetime
import socket
import cv2
import sys

class Server:
    def __init__(self, connection):
        self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.server.bind(('localhost', 4001))

        self.server.listen()
        print("waiting for connection...")

        conn, addr = self.server.accept()
        print("connected from", addr)

        self.camera = init_camera()
        self.connection = connection
        
        self.recorder = VideoRecorder(self.camera)
        self.streamer = Streamer(self.camera, conn)

        while True:
            data = conn.recv(1024)
            data = data.decode()
            print("Received:", data)

            match data.lower():
                case 'open connection':
                    print('Connection opening...')
                    conn.send('Connected\n'.encode())

                case 'start':
                    print("opening camera")
                    ret, frame = capture_frame(self.camera)
                    
                    conn.send('Camera open\n'.encode())
                    
                case 'stream':
                    self.streamer.stream()
                    
                case 'record':
                    self.recorder.record()
                    
                case 'capture':
                    capture(self.camera)
                    conn.send('Image captured\n'.encode())

                case 'stop':
                    self.streamer.stop()
                
                case 'close':
                    self.recorder.close()
                    self.streamer.close()
                    break
                    
        self.server.close()