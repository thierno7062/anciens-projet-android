'use strict';

angular.module('newsappApp')
    .controller('AuthorDetailController', function ($scope, $stateParams, Author, News) {
        $scope.author = {};
        $scope.load = function (id) {
            Author.get({id: id}, function(result) {
              $scope.author = result;
            });
        };
        $scope.load($stateParams.id);
    });
