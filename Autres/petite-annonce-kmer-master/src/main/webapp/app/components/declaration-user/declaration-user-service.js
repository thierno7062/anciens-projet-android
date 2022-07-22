/**
 * the service for managing one declaration
 * Created by admin on 29/12/2016.
 */


(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('DeclarationUserService', DeclarationUserService);

    DeclarationUserService.$inject = ['$uibModal'];

    function DeclarationUserService ($uibModal) {
        var service = {
            openNew: openNew
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function openNew () {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/declaration-user/declaration-user-dialog.html',
                controller: 'DeclarationUserController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('declaration-user');
                        $translatePartialLoader.addPart('register');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            title: null,
                            description: null,
                            creationDate: null,
                            lastModifiedDate: null,
                            isPublished: false,
                            price: null,
                            publishedDate: null,
                            id: null
                        };
                    }
                }
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
