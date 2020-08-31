var app = angular.module('app', ['ngRoute', 'ngStorage']);
var contextPath = 'http://localhost:8189/store'

app.config(function ($routeProvider) {
    $routeProvider
        .when('/', {
            templateUrl: 'about-page.html',
            controller: 'aboutController'
        })
        .when('/books', {
            templateUrl: 'book-store.html',
            controller: 'booksController'
        })
});

app.controller('mainController', function ($scope, $http, $localStorage) {
    $scope.tryToAuth = function () {
        $http.post(contextPath + '/auth', $scope.user)
            .then(function successCallback(response) {
                if (response.data.token) {
                    $http.defaults.headers.common.Authorization = 'Bearer ' + response.data.token;
                    $localStorage.currentUser = { username: $scope.user.username, token: response.data.token };
                }
            }, function errorCallback(response) {
                window.alert(response.data.message);
                $scope.clearUser();
            });
    };

    $scope.tryToLogout = function () {
        $scope.clearUser();
    };

    $scope.isAuthorized = function () {
        return $localStorage.currentUser != null;
    };

    $scope.clearUser = function () {
        console.log("user cleared");
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
    };
});

app.controller('aboutController', function ($scope, $http, $localStorage) {
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    fillTable = function () {
        $http.get(contextPath + '/api/v1/books/dtos')
            .then(function successCallback(response) {
                $scope.PopularBooksList = response.data;
                console.log("OK");
            }, function errorCallback(response) {
                console.log(response);
                window.alert(response.data.message);

                $scope.clearUser();
            });
    }

    fillTable();

    $scope.clearUser = function () {
        console.log("user cleared");
        delete $localStorage.currentUser;
        $http.defaults.headers.common.Authorization = '';
    }
});

app.controller('booksController', function ($scope, $http, $localStorage) {
    if ($localStorage.currentUser) {
        $http.defaults.headers.common.Authorization = 'Bearer ' + $localStorage.currentUser.token;
    }

    fillTable = function () {
        $http.get(contextPath + '/api/v1/books')
            .then(function (response) {
                $scope.BooksList = response.data;
            });
    };

    $scope.submitCreateNewBook = function () {
        $http.post(contextPath + '/api/v1/books', $scope.newBook)
            .then(function (response) {
                $scope.BooksList.push(response.data);
            });
    };

    fillTable();
});