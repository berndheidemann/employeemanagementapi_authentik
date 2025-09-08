docker image für mac bauen:

in Dockerfile den Param anpassen und:

In Dockerfile das FROm und Jar anpassen

docker buildx create --use
docker buildx build --push --platform linux/arm,linux/arm64,linux/amd64 --tag
berndheidemann/employee-management-service:1.0.4 .

ggf: docker tag berndheidemann/employee-management-service:1.0.3 berndheidemann/employee-management-service:latest
ggf: docker push berndheidemann/employee-management-service:latest


