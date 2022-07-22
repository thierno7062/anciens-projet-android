/**
 * Created by admin on 29/01/2017.
 */
(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .config(html5ModeConfig);

    html5ModeConfig.$inject = ['$locationProvider'];

    function html5ModeConfig($locationProvider) {
        $locationProvider.html5Mode({ enabled: true, requireBase: true });
    }
})();
