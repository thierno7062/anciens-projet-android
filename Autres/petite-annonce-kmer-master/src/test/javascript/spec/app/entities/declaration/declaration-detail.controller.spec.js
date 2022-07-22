'use strict';

describe('Controller Tests', function() {

    describe('Declaration Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDeclaration, MockUser, MockLocalisation, MockImage;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDeclaration = jasmine.createSpy('MockDeclaration');
            MockUser = jasmine.createSpy('MockUser');
            MockLocalisation = jasmine.createSpy('MockLocalisation');
            MockImage = jasmine.createSpy('MockImage');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Declaration': MockDeclaration,
                'User': MockUser,
                'Localisation': MockLocalisation,
                'Image': MockImage
            };
            createController = function() {
                $injector.get('$controller')("DeclarationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'petiteAnnonceKmerApp:declarationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
