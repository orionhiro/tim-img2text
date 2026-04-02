import easyocr

class OcrService:
    def __init__(self):
        self.reader = easyocr.Reader(['ru', 'en'])
    
    def recognize(self, filePath):
        result = self.reader.readtext(filePath)

        return "\n".join([text for bbox, text, prob in result])