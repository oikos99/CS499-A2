(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('InstructorDetailController', InstructorDetailController);

    InstructorDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Instructor', 'Course'];

    function InstructorDetailController($scope, $rootScope, $stateParams, previousState, entity, Instructor, Course) {
        var vm = this;

        vm.instructor = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('a2App:instructorUpdate', function(event, result) {
            vm.instructor = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
