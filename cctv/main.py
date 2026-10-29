from database.connection import connect
from recording.video_recorder import record_video
from camera.camera_manager import init_camera

def main():
    connection = connect()

    try:
        camera = init_camera()
        record_video(camera)

    finally:
        connection.close()

if __name__ == '__main__':
    main()