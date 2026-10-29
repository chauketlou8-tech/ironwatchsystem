import cv2
from camera.camera_capture import capture_frame

def record_video(camera):
    while camera.isOpened():
        ret, frame = capture_frame(camera)

        if not ret:
            break

        cv2.imshow('CCTV', frame)

        key = cv2.waitKey(1)
        cond = key & 0xFF

        if cond == ord('q') or cv2.getWindowProperty('CCTV',cv2.WND_PROP_VISIBLE) < 1:
            break

    camera.release()
    cv2.destroyAllWindows()