def capture_frame(camera):
    if camera.isOpened():
        ret, frame = camera.read()
        return ret, frame

    camera.release()
    raise RuntimeError("Error capturing the frame, please make sure the camera is opened.")

def capture_image(camera):
    if camera.isOpened():
        ret, frame = capture_frame(self.camera)
        now = datetime.now().strftime("%Y_%m_%d_%H_%M_%S")
        
        image_title = "IW_CCTV_Snapshot_" + now + ".jpg"
        cv2.imwrite(image_title, frame)
        
    else:
        camera.release()
        raise RuntimeError("Error capturing the frame, please make sure the camera is opened.")