(function() {
    'use strict';
    angular
        .module('a2App')
        .factory('OfficeHour', OfficeHour);

    OfficeHour.$inject = ['$resource', 'DateUtils'];

    function OfficeHour ($resource, DateUtils) {
        var resourceUrl =  'api/office-hours/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertDateTimeFromServer(data.startTime);
                        data.endTime = DateUtils.convertDateTimeFromServer(data.endTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
