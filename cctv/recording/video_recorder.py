import cv2
from datetime import datetime, date

class VideoRecorder:
    def __init__(self, camera, camera_capture):
        self.camera = camera
        self.capture = camera_capture

        self.is_recording = False
        self.writer = None

    def stop(self):
        self.is_recording = False
        
        if self.writer is not None:
            self.writer.release()
            self.writer = None
        
    def record(self):
        if not self.camera.isOpened():
            return
        
        print('Recording...')
        
        self.is_recording = True
        
        if self.writer is None:
            today = date.today()
        
            ret, frame = self.capture.capture_frame()
            
            if not ret:
                return
            
            self.height, self.width = frame.shape[:2]
            self.writer = cv2.VideoWriter(f"recording-{today}.avi", cv2.VideoWriter_fourcc(*'MJPG'), 12, (self.width, self.height))
            
            if not self.writer.isOpened():
                self.writer = None
                return
        
        ret, frame = self.capture.capture_frame()
        
        if not ret:
            return
        
        now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        cv2.putText(frame, now, (20, 36), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1)
        
        cv2.circle(frame, (550, 30), 6, (0, 0, 255), -1)
        cv2.putText(frame, "Recording", (565, 36), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
        self.writer.write(frame)
        