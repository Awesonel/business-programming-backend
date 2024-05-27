## Как поднять в Kubernetes

### До

1. Обновить Docker Desktop до последней версии в настройках
2. Включить Kubernetes в настройках

### Установить Chocolatey:

Запустить powershell от имени админа

Вписать
```shell
Set-ExecutionPolicy AllSigned
```
Запустить
```shell
Set-ExecutionPolicy Bypass -Scope Process -Force; [System.Net.ServicePointManager]::SecurityProtocol = [System.Net.ServicePointManager]::SecurityProtocol -bor 3072; iex ((New-Object System.Net.WebClient).DownloadString('https://community.chocolatey.org/install.ps1'))
```

### Установить kubernetes-helm
```shell
choco install kubernetes-helm
```
#### !!! Если не находит `choco` после установки, то перезапустить терминал/IDE !!!

### Сгенерировать пароль для postgres
```shell
kubectl create secret generic pgpassword --from-literal PGPASSWORD=12345
```
### Установить ingress controller
```shell
helm upgrade --install ingress-nginx ingress-nginx --repo https://kubernetes.github.io/ingress-nginx --namespace ingress-nginx --create-namespace
```

### Билдим образы

Зайти в папку бекенда

В `docker-compose.yml` в разделе `frontend build context` поменять `context` на путь к папке с фронтом

Сбилдить докер образы

```shell
docker-compose build -p business_project
```

### Поднимаем проект
```shell
kubectl apply -f k8s -R
```

### Проверяем
Фронт: http://localhost

Документация: http://localhost/api/swagger-ui/index.html

## Поднимаем дашборд

### Установить дашборд
```shell
helm repo add kubernetes-dashboard https://kubernetes.github.io/dashboard/
```
```shell
helm upgrade --install kubernetes-dashboard kubernetes-dashboard/kubernetes-dashboard --create-namespace --namespace kubernetes-dashboard
```
#### Возможно нужно будет немного подождать, пока поднимутся все поды
#### Проверить можно через
```shell
kubectl get pods -n kubernetes-dashboard
```
#### Нужно дождаться пока всё будет `Running`

### Пробросить порты
```shell
kubectl -n kubernetes-dashboard port-forward svc/kubernetes-dashboard-kong-proxy 8443:443
```

### Применить конфиги для дашборда
```shell
kubectl apply -f k8s/dashboard 
```

### Получить токен для дашборда
Запустить Git Bash (можно найти в поиске винды)

Вписать
```shell
kubectl get secret admin-user -n kubernetes-dashboard -o jsonpath={".data.token"} | base64 -d
```
Скопировать токен

### Зайти в дашборд
Ссылка https://localhost:8443

### Если обновились докер образы

Билдим
```shell
docker-compose build -p business_project

# или если нужно только фронт
docker-compose build -p business_project frontend

# или только бек
docker-compose build -p business_project backend
```

Перезапускаем нужный деплой через дашборд в разделе `Deployments` (нажать на три точки)
