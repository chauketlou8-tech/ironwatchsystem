from database.connection import connect
from sockets.server import Server

def main():
    connection = connect()
    Server(connection)

if __name__ == '__main__':
    main()