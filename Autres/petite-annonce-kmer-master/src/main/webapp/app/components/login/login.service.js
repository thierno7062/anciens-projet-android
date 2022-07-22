(function() {
    'use strict';

    angular
        .module('petiteAnnonceKmerApp')
        .factory('LoginService', LoginService);

    LoginService.$inject = ['$uibModal'];

    function LoginService ($uibModal) {
        var service = {
            open: open
        };

        var modalInstance = null;
        var resetModal = function () {
            modalInstance = null;
        };

        return service;

        function open (isSaveDeclaration,declarationUibModalInstance,declaration) {
            if (modalInstance !== null) return;
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/components/login/login.html',
                controller: 'LoginController',
                controllerAs: 'vm',
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('login');
                        return $translate.refresh();
                    }],
                    isSaveDeclaration:isSaveDeclaration,
                    declarationUibModalInstance:declarationUibModalInstance,
                    declaration:declaration
                }
            });
            modalInstance.result.then(
                resetModal,
                resetModal
            );
        }
    }
})();
