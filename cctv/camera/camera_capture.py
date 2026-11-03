import cv2
from datetime import datetime

class CameraCapture:
    def __init__(self, camera):
        self.camera = camera
    
    def capture_frame(self):
        if self.camera.isOpened():
            ret, frame = self.camera.read()
            return ret, frame
    
        self.camera.release()
        self.camera = None
        raise RuntimeError("Error capturing the frame, please make sure the camera is opened.")

    def capture_image(self):
        if self.camera.isOpened():
            ret, frame = self.capture_frame()
            
            if not ret:
                return
            
            now = datetime.now().strftime("%Y_%m_%d_%H_%M_%S")
            
            image_title = "IW_CCTV_Snapshot_" + now + ".jpg"
            cv2.imwrite(image_title, frame)
        
        else:
            self.camera.release()
            self.camera = None
            raise RuntimeError("Error capturing the frame, please make sure the camera is opened.")