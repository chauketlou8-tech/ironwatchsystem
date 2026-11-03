import cv2
import uuid
import requests
import socket
from .Camera import Camera
from datetime import datetime

class CameraManager(object):
    def __init__(self):
        self.camera = None
    
    def init_camera(self):
        self.camera = Camera("TestCamera")
    
        if self.camera.is_opened():
            return self.camera
    
        self.camera.release()
        raise RuntimeError("Could not initialize camera.")

    def close_camera(self):
        if self.camera is not None:
            self.camera.close()
            self.camera = None