package org.sagemath.droid.dialogs;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import org.sagemath.droid.R;
import org.sagemath.droid.constants.IntConstants;
import org.sagemath.droid.database.SageSQLiteOpenHelper;
import org.sagemath.droid.models.database.Cell;
import org.sagemath.droid.models.database.Group;
import org.sagemath.droid.utils.AnimationHelper;
import org.sagemath.droid.views.FloatLabelLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>The {@link android.support.v4.app.DialogFragment} which is used to add or delete a cell</p>
 * @author Nikhil Peter Raj
 */
public class CellDialogFragment extends BaseActionDialogFragment {

    private static final String TAG = "SageDroid:EditCellDialogFragment";

    private static final String ARG_CELL = "Cell";
    private static final String ARG_TYPE = "cellType";

    private LinearLayout groupContainer, dialogContainer;
    private EditText titleEditText, descriptionEditText;
    private AutoCompleteTextView groupEditText;
    private ImageButton emptyInfoButton;

    private ArrayList<Cell> cells;
    private int type;

    SageSQLiteOpenHelper helper;

    public static CellDialogFragment newInstance(Cell cell) {

        CellDialogFragment frag = new CellDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_CELL, cell);

        frag.setArguments(args);

        return frag;
    }

    public static CellDialogFragment newInstance(ArrayList<Cell> cells, int type) {

        CellDialogFragment frag = new CellDialogFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CELL, cells);
        args.putInt(ARG_TYPE, type);

        frag.setArguments(args);

        return frag;
    }

    private boolean isCellTitleEmpty() {
        return TextUtils.isEmpty(titleEditText.getText());
    }

    private boolean isCellGroupPlayground() {
        return groupEditText.getText().toString().equalsIgnoreCase(getString(R.string.group_playground));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        cells = getArguments().getParcelableArrayList(ARG_CELL);
        type = getArguments().getInt(ARG_TYPE);

        helper = SageSQLiteOpenHelper.getInstance(getActivity());

        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_cell_general, null);

        FloatLabelLayout nameContainer = (FloatLabelLayout) dialogView.findViewById(R.id.nameContainer);
        FloatLabelLayout descriptionContainer = (FloatLabelLayout) dialogView.findViewById(R.id.descriptionContainer);

        groupContainer = (LinearLayout) dialogView.findViewById(R.id.groupContainer);
        dialogContainer = (LinearLayout) dialogView.findViewById(R.id.dialogContainer);

        titleEditText = (EditText) dialogView.findViewById(R.id.insert_cell_title);
        descriptionEditText = (EditText) dialogView.findViewById(R.id.insert_cell_desc);
        groupEditText = (AutoCompleteTextView) dialogView.findViewById(R.id.insert_cell_group);

        titleEditText.setHint(R.string.dialog_name_hint);
        groupEditText.setHint(R.string.dialog_group_hint);
        descriptionEditText.setHint(R.string.dialog_description_hint);

        emptyInfoButton = (ImageButton) dialogView.findViewById(R.id.emptyInfoButton);
        emptyInfoButton.setVisibility(View.VISIBLE);
        emptyInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), R.string.dialog_info_button_contentDescription, Toast.LENGTH_SHORT).show();
            }
        });

        List<Group> cellGroups = helper.getGroups();
        ArrayList<String> groupStrings = new ArrayList<>();
        for (Group group : cellGroups) {
            groupStrings.add(group.getCellGroup());
        }
        String[] groupChoices = new String[groupStrings.size()];
        groupStrings.toArray(groupChoices);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, groupChoices);
        groupEditText.setAdapter(adapter);

        groupEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    emptyInfoButton.setVisibility(View.VISIBLE);
                } else {
                    emptyInfoButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);

        switch (type) {
            case IntConstants.DIALOG_NEW_GROUP:
                //Hide name and description
                nameContainer.setVisibility(View.GONE);
                descriptionContainer.setVisibility(View.GONE);
                builder.setTitle(R.string.dialog_new_group_title);
                break;

            case IntConstants.DIALOG_EDIT_CELL:
                if (cells.size() == 1) {
                    builder.setTitle(R.string.dialog_edit_cell_single_title);
                    Cell cell = cells.get(0);
                    titleEditText.setText(cell.getTitle());
                    groupEditText.setText(cell.getGroup().getCellGroup());
                    descriptionEditText.setText(cell.getDescription());
                } else {
                    nameContainer.setVisibility(View.GONE);
                    descriptionContainer.setVisibility(View.GONE);
                    groupEditText.setHint(R.string.dialog_edit_cell_multiple_group_hint);
                    builder.setTitle(R.string.dialog_edit_cell_multiple_title);
                }
                break;

            case IntConstants.DIALOG_NEW_CELL:
                builder.setTitle(R.string.dialog_new_cell_title);
                break;

        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Empty button impl for older versions
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        return builder.create();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();

        if (d != null) {
            Button positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);

            switch (type) {
                case IntConstants.DIALOG_NEW_GROUP:
                    //TODO remove this
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Add group here
                        }
                    });
                    break;

                case IntConstants.DIALOG_EDIT_CELL:
                    if (cells.size() == 1) {
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isCellTitleEmpty() || isCellGroupPlayground()) {
                                    if (AnimationHelper.isIcsOrAbove()) {
                                        AnimationHelper.Nope(dialogContainer).start();
                                    } else {
                                        AnimationHelper.SupportNope(dialogContainer).start();
                                    }
                                } else {
                                    Cell cell = cells.get(0);
                                    cell.setTitle(titleEditText.getText().toString());
                                    cell.setDescription(descriptionEditText.getText().toString());
                                    Group group;
                                    if (TextUtils.isEmpty(groupEditText.getText())) {
                                        group = new Group(getString(R.string.group_misc));
                                    } else {
                                        group = new Group(groupEditText.getText().toString());
                                    }
                                    cell.setGroup(group);
                                    helper.addGroup(group);
                                    helper.saveEditedCell(cell);
                                    listener.onActionCompleted();
                                    dismiss();
                                }
                            }
                        });
                    } else {
                        positiveButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isCellTitleEmpty()) {
                                    if (AnimationHelper.isIcsOrAbove()) {
                                        AnimationHelper.Nope(dialogContainer).start();
                                    } else {
                                        AnimationHelper.SupportNope(dialogContainer).start();
                                    }
                                } else {

                                    Group group = new Group(groupEditText.getText().toString());
                                    for (Cell cell : cells) {
                                        cell.setGroup(group);
                                    }
                                    helper.saveGroup(group);
                                    helper.saveEditedCells(cells);
                                    listener.onActionCompleted();
                                    dismiss();
                                }
                            }
                        });
                    }
                    break;
                case IntConstants.DIALOG_NEW_CELL:
                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isCellTitleEmpty() || isCellGroupPlayground()) {
                                if (AnimationHelper.isIcsOrAbove()) {
                                    AnimationHelper.Nope(dialogContainer).start();
                                } else {
                                    AnimationHelper.SupportNope(dialogContainer).start();
                                }
                            } else {

                                Cell cell = new Cell();
                                cell.setTitle(titleEditText.getText().toString());
                                Group group;
                                if (TextUtils.isEmpty(groupEditText.getText().toString())) {
                                    group = new Group(getString(R.string.group_misc));
                                } else {
                                    group = new Group(groupEditText.getText().toString());
                                }
                                cell.setGroup(group);
                                cell.setDescription(descriptionEditText.getText().toString());
                                helper.addGroup(group);
                                helper.addCell(cell);
                                listener.onActionCompleted();
                                dismiss();
                            }
                        }
                    });
                    break;
            }


        }
    }
}

