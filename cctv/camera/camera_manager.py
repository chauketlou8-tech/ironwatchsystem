import cv2

def init_camera():
    camera = cv2.VideoCapture(0)

    if camera.isOpened():
        return camera

    camera.release()
    raise RuntimeError("Could not initialize camera.")