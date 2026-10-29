import cv2
from camera.camera_capture import capture_frame
from datetime import datetime, date

def record_video(camera):
    ret, frame = camera.read()
    height, width = frame.shape[:2]

    today = date.today()

    writer = cv2.VideoWriter(f"recording-{today}.avi", cv2.VideoWriter_fourcc(*'MJPG'), 12, (width, height))
    image_count = 1

    while camera.isOpened():
        ret, frame = capture_frame(camera)

        if not ret:
            break


        now = datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        cv2.putText(frame, now, (20, 36), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 0), 1)

        cv2.imshow('CCTV', frame)
        writer.write(frame)

        key = cv2.waitKey(1)
        cond = key & 0xFF

        if cond == ord('q') or cv2.getWindowProperty('CCTV',cv2.WND_PROP_VISIBLE) < 1:
            break

        elif cond == ord(' '):
            cv2.imwrite(f'CCTV_image{image_count}.jpg',frame)


    camera.release()
    writer.release()
    cv2.destroyAllWindows()