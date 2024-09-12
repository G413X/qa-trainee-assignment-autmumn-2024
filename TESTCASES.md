
### ПОЛУЧЕНИЕ ОБЪЯВЛЕНИЙ ПО ID (https://qa-internship.avito.com/api/1/item/:id)

1) 
- summary: |
    Возврат 200 при запросе существующего ID
  requirement: |
    Запрос по существующему ID объявления должен возвращать 200 (OК)
  prerequisites: |
   Объявление по запрашиваемому ID существует 
  test-data: |
    ID: d3523e5d-2c17-46b7-857e-cd59f79a1598
  steps:
    - Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с указанным ID
  expected-result: |
    Код ответа: 200 OK
- autotest:  см. метод ***"shouldReturn200WhenIdIsExisting"*** в ./src/test/java/api/GetAdsByIdsTest.java

2) 
- summary: |
    Возврат 404 при запросе невалидного ID
  requirement: |
    Запрос по невалидному ID объявления должен возвращать 404
  prerequisites: |
   - 
  test-data: |
    no data
  steps:
    -Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с пустым ID
  expected-result: |
    Код ответа: 404 OK
- autotest:  см. метод ***"ShouldReturn404WhenIdIsInvalid"*** в ./src/test/java/api/GetAdsByIdsTest.java

3) 
- summary: |
    Проверка, что для существующего ID возвращаются все ожидаемые поля и их значения не null
  requirement: |
    По существующему ID должны возвращаться все ожидаемые поля и иметь не null значения
  prerequisites: |
   Объявление по запрашиваемому ID существует
  test-data: |
    ID: d3523e5d-2c17-46b7-857e-cd59f79a1598
  steps:
    - Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с указанным ID
  expected-result: |
  Ответ содержит все ожидаемые поля с не null значениями, структура ответа соответствует следующему примеру:
  [
    {
        "createdAt": "2024-09-08 18:51:53.942364 +0300 +0300",
        "id": "d3523e5d-2c17-46b7-857e-cd59f79a1598",
        "name": "Телефон",
        "price": 85566,
        "sellerId": 3452,
        "statistics": {
            "contacts": 32,
            "likes": 0,
            "viewCount": 14
        }
    }
]
- autotest:  см. метод ***"ShouldContainExpectedFieldsAndNonNullValues_WhenExistingIdIsProvided"*** в ./src/test/java/api/GetAdsByIdsTest.java

4) 
- summary: |
    ID объявления в запросе совпадает с ID объявления в теле ответа
  requirement: |
	    GET запрос должен возвращать то объявление, которое запрашивается по ID
  prerequisites: |
   Объявление по запрашиваемому ID существует 
  test-data: |
    ID: d3523e5d-2c17-46b7-857e-cd59f79a1598
  steps:
    - Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с указанным ID
  expected-result: |
    ID в параметрах GET запроса совпадает с ID в теле ответа; 
- autotest:  см. метод ***"shouldReturnSameIdInResponse_WhenExistingIsProvided"*** в ./src/test/java/api/GetAdsByIdsTest.java

5) 
- summary: |
    Для недопустимых HTTP-методов запрос к существующему ID возвращает статус 405.
  requirement: |
	API должен возвращать ***405 Method Not Allowed*** при использовании недопустимых HTTP-методов.
  prerequisites: |
   Объявление по запрашиваемому ID существует 
  test-data: |
    ID: d3523e5d-2c17-46b7-857e-cd59f79a1598
  steps:
    - Отправить запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с указанным ID c использованием недопустимых методов: POST, PUT, PATCH, DELETE, HEAD, OPTIONS
  expected-result: |
    Код ответа: ***405 Method Not Allowed*** для всех указанных HTTP-методов.
- autotest:  см. параметризованный метод ***"shouldReturn405ForInvalidHttpMethodsOnGetAd"*** в ./src/test/java/api/GetAdsByIdsTest.java

### ПОЛУЧЕНИЕ ВСЕХ ОБЪЯВЛЕНИЙ ПО ПРОДАВЦАМ (https://qa-internship.avito.com/api/1/:sellerID/item)

6) 
- summary: |
    В ответе на GET по sellerId все объявления принадлежат запрашиваемому продавцу
  requirement: |
	Все объявления продавца должны иметь одинаковый с ним SellerId  
  prerequisites: |
   Продавец под запрашиваемым SellerId существует в системе
   У данного продавца имеется одно или более объявлений
  test-data: |
    SellerId: 3452
  steps:
    - Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/:sellerID/item с указанным SellerId
  expected-result: |
    Все полученные объявления имеют одинаковый с продавцом SellerId
- autotest:  см. метод ***"testAllAdsBelongToTheSameSeller"*** в ./src/test/java/api/GetSellersAdsTest.java

7) 
- summary: |
    Для валидных значений SellerID возвращается статус 200 OK. 
  requirement: |
	API должен возвращать статус 200 OK при запросе с валидными значениями SellerID.
  prerequisites: |

  test-data: |
    SellerID: 111111, 500000, 999999.
  steps:
    - Отправить GET запросы на ручку https://qa-internship.avito.com/api/1/:sellerID/item с указанными значениями SellerId
  expected-result: |
    Код ответа: 200 OK для всех валидных SellerID.
- autotest:  см. параметризованный метод ***"shouldReturn200WhenSellerIdIsValid"*** в ./src/test/java/api/GetSellersAdsTest.java

8) 
- summary: |
    Для невалидных значений SellerID возвращается код статуса, отличный от 200 OK.
  requirement: |
	API должен возвращать ошибку (код статуса не в диапазоне 200-299) при запросе с невалидными значениями SellerID.
  prerequisites: |

  test-data: |
    SellerId: 111110, 0, -1, 10000000.
  steps:
    - Отправить GET запрос на ручку https://qa-internship.avito.com/api/1/sellerID/item с указанными SellerId
  expected-result: |
    Код ответа не принадлежит к 2xx для всех валидных SellerID.
- autotest:  см. параметризованный метод ***"shouldNotReturn200WhenSellerIdIsInvalid"*** в ./src/test/java/api/GetSellersAdsTest.java

9) 
- summary: |
    Заголовок ответа содержит корректный `Content-Type` для запроса всех объявлений по продавцу.
  requirement: |
    Ответ должен возвращать заголовок `Content-Type: application/json`.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе
  test-data: |
    SellerID: 3452
  steps:
    - Отправить запрос GET на ручку https://qa-internship.avito.com/api/1/sellerID/item с указанным `sellerID`.
    - Проверить, что код ответа равен 200
    - Проверить, что в заголовке ответа присутствует `Content-Type: application/json`
  expected-result: |
    Код ответа: 200 OK. Заголовок ответа содержит `Content-Type: application/json`.
- autotest:  см. метод "***shouldReturnCorrectContentType***" в ./src/test/java/api/GetSellersAdsTest.java.

10) 
- summary: |
    В ответе возвращается массив JSON с объявлениями продавца.
  requirement: |
    Запрос на получение объявлений продавца должен возвращать массив JSON.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе
  test-data: |
    SellerID: 3452
  steps:
    - Отправить запрос GET на ручку https://qa-internship.avito.com/api/1/sellerID/item с указанным `sellerID`.
  expected-result: |
    Код ответа: 200 OK. Корневой элемент ответа является массивом, который может быть пустым или содержать объявления.
- autotest:  см. метод "***shouldReturnArrayOfAds***" в ./src/test/java/api/GetSellersAdsTest.java.

11) 
- summary: |
    Для неподдерживаемых HTTP-методов возвращается 405 Method Not Allowed.
  requirement: |
    Ручка должна возвращать ошибку 405 при попытке использовать неподдерживаемые методы HTTP.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе
  test-data: |
    SellerID: 3452
  steps:
    - Отправить запросы с неподдерживаемыми HTTP-методами (POST, PUT, PATCH, DELETE, HEAD, OPTIONS) на ручку https://qa-internship.avito.com/api/1/sellerID/item.
  expected-result: |
    Код ответа: 405 Method Not Allowed для всех неподдерживаемых HTTP-методов.
- autotest:  см. параметризованный метод "***shouldReturn405ForInvalidHttpMethods***" в ./src/test/java/api/GetSellersAdsTest.java.

### СОХРАНЕНИЕ ОБЪЯВЛЕНИЙ (https://qa-internship.avito.com/api/1/item)

12) 
- summary: |
    Проверка успешного создания объявления с валидными данными.
  requirement: |
    Объявление должно успешно создаваться при отправке валидных данных.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - Название: Телефон. 
    - Цена: 85566
    - SellerID: 600100
    - Статистика:
       - Контакты: 10
       -  Лайки: 10
       - Просмотры: 10
steps:
    - Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса: 
	  
 ```JSON
    {
        "name": "Телефон",
        "price": 85566,
        "sellerId": 600100,
        "statistics": {
            "contacts": 10,
            "likes": 10,
            "viewCount": 10
        }
    }
```

- Проверить, что статус ответа равен 200.
- Проверить, что в ответе присутствует сообщение об успешном сохранении объявления и что оно содержит валидный ID.
- Отправить GET-запрос на ручку https://qa-internship.avito.com/api/1/item/{id} с полученным ID
expected-result: |
- GET-запрос возвращает объявление с нужным ID и соответствующими созданным значениями полей и статистики.
- autotest:  см. метод "***shouldCreateAdSuccessfully***" в ./src/test/java/api/PostAdsTest.java.

13) 
- summary: |
    Для невалидных значений поля `name` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `name` сервер должен возвращать ошибку 400.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - -10 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Пробел: " "
    - Специальные символы: "@@@!!!"
    - Массив: ["Телефон"]
    - Объект: {"type": "Телефон"}
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения `name`
```JSON
  
    {
        "name": "{invalid_name_value}",
        "price": 85566,
        "sellerId": 600100,
        "statistics": {
            "contacts": 32,
            "likes": 35,
            "viewCount": 14
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenNameIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

14) 
- summary: |
    Для невалидных значений поля `price` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `price` сервер должен возвращать ошибку 400.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - -100 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Текстовое значение: "text"
    - Массив: ["Телефон"]
    - Объект: {"type": "Телефон"}
	- Дробные значения: 10.5
	  Числа через пробел: 10 10
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения `price`
```JSON
  
    {
        "name": "Телефон",
        "price": "{invalid_price_value}",
        "sellerId": 600100,
        "statistics": {
            "contacts": 32,
            "likes": 35,
            "viewCount": 14
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenPriceIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

15) 
- summary: |
    Для невалидных значений поля `sellerId` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `sellerId` сервер должен возвращать ошибку 400.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - -100 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Текстовое значение: "text"
    - Дробное значение: 600100.5
    - Массив: [600100]
    - Объект: {\"id\": 600100}
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения sellerId
```JSON
  
    {
        "name": "Телефон",
        "price": 85566,
        "sellerId": "{invalid_price_value}",
        "statistics": {
            "contacts": 32,
            "likes": 35,
            "viewCount": 14
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenSellerIdIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

16) 
- summary: |
    Для невалидных значений поля `contacts` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `contacts` сервер должен возвращать ошибку 400.
  prerequisites: |
    
  test-data: |
    - -10 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Текстовое значение: "text"
    - Массив: [32]
    - Объект: {"count": 32  }
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения contacts
```JSON
  
    {
        "name": "Телефон",
        "price": 85566,
        "sellerId": 600100
        "statistics": {
            "contacts": "{invalid_price_value}",
            "likes": 35,
            "viewCount": 14
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenContactsIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

17) 
- summary: |
    Для невалидных значений поля `likes` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `likes` сервер должен возвращать ошибку 400.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - -1 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Текстовое значение: "text"
    - Массив: [35]
    - Объект: {"count": 35 }
    - Дробное число: 30.5
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения `likes`
```JSON
  
    {
        "name": "Телефон",
        "price": 85566,
        "sellerId": 600100
        "statistics": {
            "contacts": 32,
            "likes": "{invalid_price_value}",
            "viewCount": 14
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenLikesIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

18) 
- summary: |
    Для невалидных значений поля `viewCount` возвращается ошибка 400.
  requirement: |
    При отправке запроса с невалидными данными в поле `viewCount` сервер должен возвращать ошибку 400.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    - -10 (отрицательное число) 
    - null
    - Пустая строка: ""
    - Текстовое значение: "text"
    - Массив: [14]
    - Объект: {"count": 14}
    - Дробное число: 15.5
  steps:
    Отправить POST-запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса для каждого приведённого невалидного значения `viewCount`
```JSON
  
    {
        "name": "Телефон",
        "price": 85566,
        "sellerId": 600100
        "statistics": {
            "contacts": 32,
            "likes": 35,
            "viewCount": "{invalid_price_value}"
        }
    }
```
  expected-result: |
    В каждом случае запрос возвращает ошибку 400 Bad Request.
- autotest:  см. параметризованный метод"***shouldReturn400WhenViewCountIsInvalid***" в ./src/test/java/api/PostAdsTest.java.

19) 
- summary: |
    Проверка, что невалидные HTTP-методы возвращают ошибку 405 (Method Not Allowed).
  requirement: |
    При использовании невалидных HTTP-методов на ручке `/api/1/item` сервер должен возвращать ошибку 405.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
    SellerID: 3452
  steps:
    Отправить запросы на ручку https://qa-internship.avito.com/api/1/item с использованием каждого из следующих неподдерживаемых HTTP-методов (GET, PUT, PATCH, DELETE, HEAD, OPTIONS):
  expected-result: |
    В каждом случае сервер возвращает статус 405 Method Not Allowed.
- autotest:  см. параметризованный метод"***shouldReturn405ForInvalidHttpMethods***" в ./src/test/java/api/PostAdsTest.java.

20) 
- summary: |
    Проверка, что при передаче невалидного Content-Type запрос возвращает ошибку 400 (Bad Request).
  requirement: |
    Сервер должен вернуть ошибку 400 при попытке создания объявления с неправильным Content-Type.
  prerequisites: |
    Продавец с указанным `sellerID` существует в системе.
  test-data: |
  ```json
      {
        "name": "Телефон",
        "price": 10,
        "sellerId": 600100,
        "statistics": {
            "contacts": 100,
            "likes": 24,
            "viewCount": 14
        }
    }
```
  steps:
    Отправить POST запрос на ручку https://qa-internship.avito.com/api/1/item с телом запроса, как указано в `test-data`, но указать заголовок Content-Type: text/plain
  expected-result: |
    В сервер возвращает статус 400 Bad request
- autotest:  см. параметризованный метод"***shouldReturn400ForInvalidContentType***" в ./src/test/java/api/PostAdsTest.java.

