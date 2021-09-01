# demo-proxy
Wire Bot that uses Proxy

# exposed web hook
POST    /api/wire

# build image
docker build -t echo:latest .

# run container
docker run --name echo --rm echo:latest -e SERVICE_TOKEN='your service token' -e ROMAN_URL='URL for your roman' -e APP_KEY='your app key'

