from .Camera import Camera

class CameraManager(object):
    def __init__(self, connection):
        self.camera = None
        self.connection = connection
        self.cur = connection.cursor()
    
    def init_camera(self):
        self.camera = Camera("TestCamera")
    
        if self.camera.is_opened():
            self.cur.execute("select camera_id, camera_name, camera_type, location, ip_address, version, status, installation_date from cameras where camera_name = %s", (self.camera.name,))
            
            exists_camera = self.cur.fetchone()
            
            if not exists_camera:
                self.cur.execute("insert into cameras(camera_id, camera_name, camera_type, location, ip_address, version, status) values (%s, %s, %s, %s, %s, %s, %s)",
                            (self.camera.id, self.camera.name, self.camera.type, self.camera.location, self.camera.ip, self.camera.version, self.camera.status))
                
                self.connection.commit()

                return self.camera
            
            print("camera already initialized skipping...")
            self.camera.id = exists_camera[0]
            self.camera.name = exists_camera[1]
            self.camera.type = exists_camera[2]
            self.camera.location = exists_camera[3]
            self.camera.ip = exists_camera[4]
            self.camera.version = exists_camera[5]
            self.camera.status = exists_camera[6]
            self.camera.installation_date = exists_camera[7]
            
            return self.camera
    
        self.camera.release()
        raise RuntimeError("Could not initialize camera.")

    def close_camera(self):
        if self.camera is not None:
            self.camera.close()
            self.camera = None