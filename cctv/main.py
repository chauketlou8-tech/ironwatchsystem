from database.connection import connect
from recording.video_recorder import VideoRecorder
from camera.camera_manager import init_camera
import keyboard

def main():
    connection = connect()

    try:
        camera = init_camera()

        videoRecorder = VideoRecorder(camera)
        videoRecorder.start()

    finally:
        connection.close()

if __name__ == '__main__':
    main()