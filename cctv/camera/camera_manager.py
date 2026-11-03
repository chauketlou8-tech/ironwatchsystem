import cv2

class CameraManager(object):
    def __init__(self):
        self.camera = None
    
    def init_camera(self):
        self.camera = cv2.VideoCapture(0)
    
        if self.camera.isOpened():
            return self.camera
    
        self.camera.release()
        raise RuntimeError("Could not initialize camera.")

    def close_camera(self):
        if self.camera is not None:
            self.camera.release()
            self.camera = None