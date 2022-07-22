'use strict';

angular.module('newsappApp')
    .controller('NewsDetailController', function ($scope, $stateParams, News) {
        $scope.news = {};
        $scope.load = function (id) {
            News.get({id: id}, function(result) {
              $scope.news = result;
            });
        };
        $scope.load($stateParams.id);
    });
