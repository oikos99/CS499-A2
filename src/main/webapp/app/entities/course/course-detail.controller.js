(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('CourseDetailController', CourseDetailController);

    CourseDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Course', 'OfficeHour', 'Student', 'Project', 'Instructor'];

    function CourseDetailController($scope, $rootScope, $stateParams, previousState, entity, Course, OfficeHour, Student, Project, Instructor) {
        var vm = this;

        vm.course = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('a2App:courseUpdate', function(event, result) {
            vm.course = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
