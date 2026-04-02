from minio import Minio
from minio.error import S3Error

class MinioService:
    def __init__(self, url, access_key, secret_key, secure=False):
        self.minio_client = Minio(
            endpoint=url,
            access_key=access_key,
            secret_key=secret_key,
            secure=secure
        )
        

    def getFile(self, bucketName, fileName, filePath):
        try:
            self.minio_client.fget_object(
                bucketName,
                fileName,
                filePath
            )
            return True
        except S3Error as err:
            print(f"[Minio] Download failed: {err}")
            return False