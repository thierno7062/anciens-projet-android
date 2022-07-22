'use strict';

angular.module('newsappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('news', {
                parent: 'entity',
                url: '/news',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/news/newss.html',
                        controller: 'NewsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('news');
                        return $translate.refresh();
                    }]
                }
            })
            .state('newsDetail', {
                parent: 'entity',
                url: '/news/:id',
                data: {
                    roles: ['ROLE_USER']
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/news/news-detail.html',
                        controller: 'NewsDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('news');
                        return $translate.refresh();
                    }]
                }
            });
    });
