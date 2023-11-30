package ru.nk.rschir.general.requests;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.nk.rschir.authentication.AuthService;
import ru.nk.rschir.types.EntityWithMerge;
import ru.nk.rschir.types.ResponseWithStatus;
import ru.nk.rschir.types.StatusCode;
import ru.nk.rschir.types.functions.FindAll;
import ru.nk.rschir.types.functions.Func2Args;
import ru.nk.rschir.types.functions.VoidReturnFunc;
import ru.nk.rschir.users.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class ServiceFunctions {
    private final AuthService authService;

    public boolean isUser(HttpServletRequest request) {
        return authService.isUser(request);
    }
    public boolean isNotAdmin(HttpServletRequest request) {
        return authService.isNotAdmin(request);
    }
    public User getUserByHttpRequest(HttpServletRequest request) {
        return authService.getUserByHttpRequest(request);
    }
    public <ObjectType> ResponseWithStatus<List<ObjectType>> findAllWithAuth(
            FindAll<ObjectType> findAll,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        return ResponseWithStatus.create(200, findAll.apply());
    }
    public <ObjectType, PropType> ResponseWithStatus<List<ObjectType>> findAllByWithAuth(
            PropType property,
            Function<PropType, List<ObjectType>> findAllBy,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        return ResponseWithStatus.create(200, findAllBy.apply(property));
    }
    public <ObjectType, PropType, ResType> ResponseWithStatus<List<ResType>> findAllByWithAuthWithLists(
            PropType property,
            Function<PropType, List<ObjectType>> findFunction,
            Function<ObjectType, ResType> mapFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        List<ObjectType> response = findFunction.apply(property);

        return ResponseWithStatus.create(
                200,
                response.stream().map(mapFunction).toList()
        );
    }
    public <ObjectType, PropType> ResponseWithStatus<ObjectType> findBy(
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction
    ) {
        ObjectType object = findFunction.apply(property).orElse(null);
        if (object == null) {
            return ResponseWithStatus.<ObjectType>builder()
                    .status(404)
                    .data(null)
                    .build();
        }

        return ResponseWithStatus.<ObjectType>builder()
                .status(200)
                .data(object)
                .build();
    }
    public <ObjectType, PropType> ResponseWithStatus<ObjectType> findByWithAuth(
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.<ObjectType>builder()
                    .status(403)
                    .data(null)
                    .build();
        }

        return findBy(property, findFunction);
    }
    public <ResType, PropType> ResponseWithStatus<ResType> findByWithJoinWithAuth(
            PropType property,
            Function<PropType, List<Object[]>> findFunction,
            Function<List<Object[]>, ResType> mapFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        List<Object[]> response = findFunction.apply(property);
        if (response.size() == 0) {
            return ResponseWithStatus.empty(404);
        }

        return ResponseWithStatus.create(200, mapFunction.apply(response));
    }
    public <ObjectType, PropType, ResType> ResponseWithStatus<ResType> findByWithListWithAuth(
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            Function<ObjectType, ResType> mapFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        ObjectType response = findFunction.apply(property).orElse(null);
        if (response == null) {
            return ResponseWithStatus.empty(404);
        }

        return ResponseWithStatus.create(200, mapFunction.apply(response));
    }
    public <ObjectType, PropType1, PropType2, ResType> ResponseWithStatus<ResType> findByWithListWithAuth(
            PropType1 property1,
            PropType2 property2,
            Func2Args<PropType1, PropType2, Optional<ObjectType>> findFunction,
            Function<ObjectType, ResType> mapFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return ResponseWithStatus.empty(403);
        }

        ObjectType response = findFunction.apply(property1, property2).orElse(null);
        if (response == null) {
            return ResponseWithStatus.empty(404);
        }

        return ResponseWithStatus.create(200, mapFunction.apply(response));
    }
    public <ObjectType, PropType> StatusCode saveWithAuth(
            ObjectType object,
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            Function<ObjectType, ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        if (findFunction.apply(property).orElse(null) == null) {
            saveFunction.apply(object);
            return StatusCode.create(200);
        }

        return StatusCode.create(409);
    }
    public <ObjectType> StatusCode saveWithCheckFieldsWithAuth(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            Function<ObjectType, ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        return saveWithCheckFields(object, fieldsNotExist, saveFunction);
    }
    public <ObjectType> StatusCode saveWithCheckFields(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            Function<ObjectType, ObjectType> saveFunction
    ) {
        if (fieldsNotExist.apply(object)) {
            return StatusCode.create(404);
        }

        saveFunction.apply(object);
        return StatusCode.create(200);
    }
    public <ObjectType, PropType> StatusCode saveWithCheckFieldsWithAuth(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            Function<ObjectType, ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        if (fieldsNotExist.apply(object)) {
            return StatusCode.create(404);
        }

        if (findFunction.apply(property).orElse(null) == null) {
            saveFunction.apply(object);
            return StatusCode.create(200);
        }

        return StatusCode.create(409);
    }
    public <ObjectType, Prop1Type, Prop2Type> StatusCode saveWithCheckFieldsWithAuth(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            Prop1Type property1,
            Prop2Type property2,
            Func2Args<Prop1Type, Prop2Type, Optional<ObjectType>> findFunction,
            Function<ObjectType, ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        if (fieldsNotExist.apply(object)) {
            return StatusCode.create(404);
        }

        if (findFunction.apply(property1, property2).orElse(null) == null) {
            saveFunction.apply(object);
            return StatusCode.create(200);
        }

        return StatusCode.create(409);
    }
    public <ObjectType extends EntityWithMerge<ObjectType>, PropType> StatusCode changeWithAuth(
            ObjectType object,
            Function<PropType, Optional<ObjectType>> findFunction,
            PropType property,
            VoidReturnFunc<ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        ObjectType dbObject = findFunction.apply(property).orElse(null);
        if (dbObject == null) {
            return StatusCode.create(404);
        }

        dbObject.merge(object);
        saveFunction.apply(dbObject);
        return StatusCode.create(200);
    }
    public <ObjectType extends EntityWithMerge<ObjectType>, PropType> StatusCode changeWithCheckFieldsWithAuth(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            Function<PropType, Optional<ObjectType>> findFunction,
            PropType property,
            VoidReturnFunc<ObjectType> saveFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        if (fieldsNotExist.apply(object)) {
            return StatusCode.create(404);
        }

        ObjectType dbObject = findFunction.apply(property).orElse(null);
        if (dbObject == null) {
            return StatusCode.create(404);
        }

        dbObject.merge(object);
        saveFunction.apply(dbObject);
        return StatusCode.create(200);
    }
    public <ObjectType extends EntityWithMerge<ObjectType>, PropType> StatusCode changeWithCheckFields(
            ObjectType object,
            Function<ObjectType, Boolean> fieldsNotExist,
            Function<PropType, Optional<ObjectType>> findFunction,
            PropType property,
            VoidReturnFunc<ObjectType> saveFunction
    ) {
        if (fieldsNotExist.apply(object)) {
            return StatusCode.create(404);
        }

        ObjectType dbObject = findFunction.apply(property).orElse(null);
        if (dbObject == null) {
            return StatusCode.create(404);
        }

        dbObject.merge(object);
        saveFunction.apply(dbObject);
        return StatusCode.create(200);
    }
    public <ObjectType, PropType> StatusCode deleteByWithAuth(
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            VoidReturnFunc<PropType> deleteFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        if (findFunction.apply(property).orElse(null) == null) {
            return StatusCode.create(404);
        }

        deleteFunction.apply(property);
        return StatusCode.create(200);
    }

    public <PropType> StatusCode deleteAllByWithAuth(
            PropType property,
            VoidReturnFunc<PropType> deleteFunction,
            HttpServletRequest request
    ) {
        if (isNotAdmin(request)) {
            return StatusCode.create(403);
        }

        deleteFunction.apply(property);
        return StatusCode.create(200);
    }

    public <ObjectType, PropType> StatusCode deleteByIdWithJoinWithAuth(
            PropType property,
            Function<PropType, Optional<ObjectType>> findFunction,
            VoidReturnFunc<PropType> deleteFunction,
            VoidReturnFunc<PropType> deleteFunctionForJoin,
            HttpServletRequest request
    ) {
        StatusCode deleteRes = deleteByWithAuth(
                property,
                findFunction,
                deleteFunction,
                request
        );

        if (deleteRes.getStatus() != 200) {
            return deleteRes;
        }

        deleteFunctionForJoin.apply(property);
        return deleteRes;
    }
}
