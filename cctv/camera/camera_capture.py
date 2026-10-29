def capture_frame(camera):
    if camera.isOpened():
        ret, frame = camera.read()
        return ret, frame

    camera.release()
    raise RuntimeError("Error caputuring the frame, please make sure the camera is opened.")