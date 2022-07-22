'use strict';

describe('Controller Tests', function() {

    describe('Image Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockImage, MockDeclaration;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockImage = jasmine.createSpy('MockImage');
            MockDeclaration = jasmine.createSpy('MockDeclaration');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Image': MockImage,
                'Declaration': MockDeclaration
            };
            createController = function() {
                $injector.get('$controller')("ImageDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'petiteAnnonceKmerApp:imageUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
