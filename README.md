
## Dashboard
* обновить docker desktop а то dashboard может не работать

Установить helm
```shell
choco install kubernetes-helm
```
Установить дашборд
```shell
# Add kubernetes-dashboard repository
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
```
```shell
# Deploy a Helm Release named "kubernetes-dashboard" using the kubernetes-dashboard chart
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
```
Зайти в дашборд
```shell
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
```
Применить конфиги для дашборда
```shell
kubectl apply -f k8s/dashboard 
```
Получить токен для дашборда (ЧЕРЕЗ GIT BASH)
```shell
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
```

## Поднять сам проект
Сгенерировать пароль для postgres
```shell
kubectl create secret generic pgpassword --from-literal PGPASSWORD=12345
```

Установить ingress controller
```shell
helm upgrade --install ingress-nginx ingress-nginx --repo https://kubernetes.github.io/ingress-nginx --namespace ingress-nginx --create-namespace
```
или (если нет helm)
```shell
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.10.1/deploy/static/provider/cloud/deploy.yaml
```

Рекурсивно применить конфиги
```shell
kubectl apply -f k8s -R
```

## Всё
http://localhost
