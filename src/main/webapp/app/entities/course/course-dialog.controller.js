(function() {
    'use strict';

    angular
        .module('a2App')
        .controller('CourseDialogController', CourseDialogController);

    CourseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Course', 'OfficeHour', 'Student', 'Project', 'Instructor'];

    function CourseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Course, OfficeHour, Student, Project, Instructor) {
        var vm = this;

        vm.course = entity;
        vm.clear = clear;
        vm.save = save;
        vm.officehours = OfficeHour.query({filter: 'course-is-null'});
        $q.all([vm.course.$promise, vm.officehours.$promise]).then(function() {
            if (!vm.course.officehour || !vm.course.officehour.id) {
                return $q.reject();
            }
            return OfficeHour.get({id : vm.course.officehour.id}).$promise;
        }).then(function(officehour) {
            vm.officehours.push(officehour);
        });
        vm.students = Student.query();
        vm.projects = Project.query();
        vm.instructors = Instructor.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.course.id !== null) {
                Course.update(vm.course, onSaveSuccess, onSaveError);
            } else {
                Course.save(vm.course, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('a2App:courseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
