/**
 * list controler of declarations
 * Created by admin on 04/12/2016.
 */

(function() {
    'use strict';

    /** . */
    var DeclarationList = {
        templateUrl: 'app/components/util/declaration-list/declaration-list.component.html',
        controller: DeclarationListController,
        controllerAs: 'vm',
        bindings: {
            declarations: '<' // declarations to be displayed
        }
    };

    // register component in app
    angular
        .module('petiteAnnonceKmerApp')
        .component('declarationList', DeclarationList);


    // injection in controller
    DeclarationListController.$inject = ['$state','Image','DataUtils'];


    /**
     * Controller which handle list of declarations:
     *
     * @constructor
     */
    function DeclarationListController($state,Image,DataUtils) {
        var vm = this;

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        vm.viewDeclaration = viewDeclaration;

        vm.settings = {width: 150, height: 150, quality: 0.9};



        function viewDeclaration(declarationId) {
            //DeclarationUserService.openDetail(declarationId);
            $state.go('declaration-detail-region',
                {declarationId: declarationId}
            );
        }

    }

})();

