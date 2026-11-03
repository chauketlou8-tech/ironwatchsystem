from recording.video_recorder import VideoRecorder
from streaming.video_streamer import VideoStreamer
from camera.camera_manager import CameraManager
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

        self.connection = connection
        
        self.camera_manager = CameraManager(self.connection)
        self.camera = self.camera_manager.init_camera()

        self.recorder = VideoRecorder(self.camera)
        self.streamer = VideoStreamer(self.camera, conn)
    
        try:
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
                        frame = self.camera.capture_frame()
                        
                        conn.send('Camera open\n'.encode())
                    
                    case 'stream':
                        self.streamer.stream()
                    
                    case 'record':
                        self.recorder.record()
                    
                    case 'capture':
                        captured = self.camera.capture_image()
                        
                        if not captured:
                            raise RuntimeError("Error occured while capturing image.")
                        
                        conn.send('Image captured\n'.encode())
                    
                    case 'stop streaming':
                        self.streamer.stop()
                    
                    case 'stop recording':
                        self.recorder.stop()
                        conn.send('recording stopped\n'.encode())
                    
                    case 'close':
                        self.camera_manager.close_camera()
                        conn.send("closed\n".encode())
                        break
                    
        except Exception:
            print("An error occured!")
            sys.exit(1)
            
        finally:
            self.server.close()