import base64
from PIL import Image, ImageDraw, ImageFont
import urllib.request
import io

# Download a Marathi font
url = "https://github.com/googlefonts/noto-fonts/raw/main/hinted/ttf/NotoSansDevanagari/NotoSansDevanagari-Bold.ttf"
urllib.request.urlretrieve(url, "NotoSansDevanagari-Bold.ttf")

text = "श्री साई अपार्टमेंट सहकारी गृहनिर्माण संस्था"
font_size = 60
font = ImageFont.truetype("NotoSansDevanagari-Bold.ttf", font_size)

# Get bounding box
left, top, right, bottom = font.getbbox(text)
width = right - left + 20
height = bottom - top + 40

image = Image.new("RGBA", (width, height), (255, 255, 255, 0))
draw = ImageDraw.Draw(image)
draw.text((10, 10), text, font=font, fill="#9a3412")

buffered = io.BytesIO()
image.save(buffered, format="PNG")
img_str = base64.b64encode(buffered.getvalue()).decode()
print("data:image/png;base64," + img_str)
