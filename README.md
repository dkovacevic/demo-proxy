# demo-proxy
Wire Echo Bot that uses Roman

# exposed web hook on 8080 port
```
POST    /api/wire
```

# Liveliness probe
```
GET    /api/wire
```

# build the image
```
docker build -t echo:latest .
```

# run in a container
```
docker run --name echo --rm echo:latest \
  -e SERVICE_TOKEN='your service token' \
  -e ROMAN_URL='URL for your roman' \
  -e APP_KEY='your app key' \
  -p 8080:8080
```
