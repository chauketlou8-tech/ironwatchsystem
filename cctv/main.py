from database.connection import connect
from recording.video_recorder import VideoRecorder
from sockets.server import Server
import keyboard

def main():
    connection = connect()
    #
    # try:
    #     camera = init_camera()
    #
    #     videoRecorder = VideoRecorder(camera)
    #     videoRecorder.start()
    #
    # finally:
    #     connection.close()

    Server(connection)

if __name__ == '__main__':
    main()