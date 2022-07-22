'use strict';

angular.module('newsappApp')
    .controller('NewsController', function ($scope, News) {
        $scope.newss = [];
        $scope.loadAll = function() {
            News.query(function(result) {
               $scope.newss = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            News.save($scope.news,
                function () {
                    $scope.loadAll();
                    $('#saveNewsModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            $scope.news = News.get({id: id});
            $('#saveNewsModal').modal('show');
        };

        $scope.delete = function (id) {
            $scope.news = News.get({id: id});
            $('#deleteNewsConfirmation').modal('show');
        };

        $scope.confirmDelete = function (id) {
            News.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteNewsConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.news = {subject: null, description: null, author: null, creationDate: null, image: null, id: null};
        };
    });
