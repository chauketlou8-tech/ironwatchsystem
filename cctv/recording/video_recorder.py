import cv2
from camera.camera_capture import capture_frame
from datetime import datetime, date

class VideoRecorder:
    def __init__(self, camera):
        self.camera = camera

        ret, frame = camera.read()
        self.height, self.width = frame.shape[:2]

        self.is_recording = False

        self.image_count = 1
        today = date.today()


        self.writer = cv2.VideoWriter(f"recording-{today}.avi", cv2.VideoWriter_fourcc(*'MJPG'), 12, (self.width, self.height))

    def start(self):
        while self.camera.isOpened():
            ret, frame = capture_frame(self.camera)

            if not ret:
                break

            now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
            cv2.putText(frame, now, (20, 36), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1)

            cv2.imshow('CCTV', frame)

            if self.is_recording:
                cv2.circle(frame, (550, 30), 6, (0, 0, 255), -1)
                cv2.putText(frame, "Recording", (565, 36), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
                self.writer.write(frame)

            key = cv2.waitKey(1)
            cond = key & 0xFF

            if cond == ord('q') or cv2.getWindowProperty('CCTV',cv2.WND_PROP_VISIBLE) < 1:
                break

            elif cond == ord('r'):
                self.is_recording = True

            elif cond == ord(' '):
                cv2.imwrite(f'CCTV_image{self.image_count}.jpg',frame)
                self.image_count += 1



        self.camera.release()

        if self.writer is not None:
            self.writer.release()

        cv2.destroyAllWindows()

    def stop(self):
        self.isRecording = False

        if self.writer is not None:
            self.writer.release()