'use strict';

angular.module('newsappApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
