import cv2
import uuid
import requests
import socket
from .Camera import Camera
from datetime import datetime

class CameraManager(object):
    def __init__(self, connection):
        self.camera = None
        self.connection = connection
        self.cur = connection.cursor()
    
    def init_camera(self):
        self.camera = Camera("TestCamera")
    
        if self.camera.is_opened():
            self.cur.execute("select id from cameras where camera_id = %s", (self.camera.id,))
            
            exists_camera = self.cur.fetchone()
            print(exists_camera)
            
            if not exists_camera:
                self.cur.execute("insert into cameras(camera_id, camera_name, camera_type, location, ip_address, version, status) values (%s, %s, %s, %s, %s, %s, %s)",
                            (self.camera.id, self.camera.name, self.camera.type, self.camera.location, self.camera.ip, self.camera.version, self.camera.status))
                
                self.connection.commit()

                return self.camera
    
        self.camera.release()
        raise RuntimeError("Could not initialize camera.")

    def close_camera(self):
        if self.camera is not None:
            self.camera.close()
            self.camera = None