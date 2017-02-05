(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('InstructorController', InstructorController);

    InstructorController.$inject = ['$scope', '$state', 'Instructor'];

    function InstructorController ($scope, $state, Instructor) {
        var vm = this;

        vm.instructors = [];

        loadAll();

        function loadAll() {
            Instructor.query(function(result) {
                vm.instructors = result;
                vm.searchQuery = null;
            });
        }
    }
})();
